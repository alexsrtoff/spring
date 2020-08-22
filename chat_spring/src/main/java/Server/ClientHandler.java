package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Socket socket;
    DataInputStream in;
    DataOutputStream out;
    MainServ serv;
    String nick;

    public String getNick() {return nick;}

    public ClientHandler(MainServ serv, Socket socket){
    try {
        this.socket = socket;
        this.serv = serv;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (true) {
                        String msg = in.readUTF();
                        if(msg.startsWith("/reg")){
                            String[] tockens = msg.split(" ");
                            if(DBService.checkClient(tockens[1])){
                                sendMsg("Ник занят попробуйте друдой");
                            }else {
                                DBService.regNewClient(tockens[1], tockens[2], tockens[3]);
                                sendMsg("/regok");
                            }
                        }
                        if (msg.startsWith("/auth")) {
                            String[] tockens = msg.split(" ");
                            String newNick = DBService.getNickByLoginAndPass(tockens[1], tockens[2]);
                            if(serv.checkNick(newNick)){
                                sendMsg("Логин/ник занят. Введите другой логин");
                            }
                            else if(newNick != null){
                                sendMsg("/authok");
                                nick = newNick;
                                serv.subscribe(ClientHandler.this);
                                break;
                            }else{
                                sendMsg("Неверный логин/пароль");
                            }
                        }
                    }

                    while (true) {
                        String msg = in.readUTF();
                        if (msg.equals("/end")) {
                            out.writeUTF("/serverClosed");
                            break;
                        }
                        if(msg.startsWith("/w")) {
                            serv.sendPrivateMsg(nick, msg);
                            String[] tockens = msg.split(" ", 3);
                        }else if(msg.startsWith("/bl")){
                            String tockens[] =msg.split(" ");
                            if(DBService.getIdByNickname(tockens[1]) != null){
                                DBService.addToBlackList(nick, tockens[1]);
                                sendMsg("Пользователь: " + tockens[1] + " в черном списке.");
                                String log = "add " + tockens[1] + " to blacklist";
                                serv.broadcastClientsList();
                            }else sendMsg("Вы хотите добавить в черный список несуществующего пользователя");
                        }else serv.broadcastMsg(nick + " " +nick + ": " + msg);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }serv.unsubscribe(ClientHandler.this);
                }
            }
        }).start();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
