package Server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

@Component
public class MainServ {
    private Vector<ClientHandler> clients;
    private DBService dbService;

    @Autowired
    public MainServ(DBService dbService) {
        this.dbService = dbService;
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;
        try {
            dbService.connect();
            server = new ServerSocket(8189);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                new ClientHandler(this, socket, dbService);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dbService.disconnect();
        }
    }


    public void broadcastClientsList() {
        StringBuilder sb = new StringBuilder();
        sb.append("/clientslist ");
        for (ClientHandler o : clients) {
                sb.append(o.getNick() + " ");
        }
        String out = sb.toString();
        for (ClientHandler o : clients) {
            String clientList = dbService.isInBlacklist(o, out);
            o.sendMsg(clientList);
        }
    }


    public void subscribe(ClientHandler client){
        clients.add(client);
        System.out.println("Клиент " + client.nick +  " подключился");
        broadcastClientsList();
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
        broadcastClientsList();
        System.out.println("Клиент " + client.nick +  " отключился");
    }

    public void broadcastMsg(String msg) {
        String[] tockens = msg.split(" ", 2);
        for (ClientHandler o: clients) {
           if(dbService.getBlackList(o.getNick(), tockens[0]) == null){
               o.sendMsg(tockens[1]);
           }
        }
    }

    public boolean checkNick(String newNick) {
        boolean check = false;
        for(ClientHandler e: clients){
            if(e.nick.equals(newNick)) check = true;
        }
        return check;
    }

    public void sendPrivateMsg(String nick, String msg) {
        String[] tockens = msg.split(" ", 3);
        for (ClientHandler o: clients){
            if (tockens[1].equals(o.nick)) {
                o.sendMsg(nick + " :" + tockens[2]);
                break;
            }
        }
        for (ClientHandler o: clients){
            if(o.nick.equals(nick)){
                o.sendMsg(nick + " :" + tockens[2]);
                break;
            }
        }
    }
}
