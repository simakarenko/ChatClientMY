package academy.prog;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String cr = "";
        String to = "";
        try {
            System.out.println("Enter your login: ");
            String login = scanner.nextLine();

            System.out.println("Enter your password: ");
            String password = scanner.nextLine();
            System.out.println("List of friends online: Yes - y, No - n.");
            String markerOnOff = scanner.nextLine();
            Thread t = Thread.currentThread();
            if (markerOnOff.equals("y")) {

                try {
                    Thread thoo = new Thread(new OnOffUsersGetThread());
                    thoo.setDaemon(true);
                    thoo.start();
                    t.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Main thread break");
                }
            }
            System.out.println("Check user status: Yes - y, No - n.");
            String markerUserStatus = scanner.nextLine();
            if (markerUserStatus.equals("y")) {
                System.out.println("Enter user name: ");
                String userName = scanner.nextLine();
                UserStatusGetThread.setUserName(userName);
                try {
                    Thread thUS = new Thread(new UserStatusGetThread());
                    thUS.setDaemon(true);
                    thUS.start();
                    t.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Main thread break");
                }
            }


            System.out.println("Send a private message: Yes - y, No - n.");
            String markerTo = scanner.nextLine();
            if (markerTo.equals("y")) {
                System.out.println("Enter the recipient's login:");
                to = scanner.nextLine();
                PrivateGetThread.setFrom(login);
                PrivateGetThread.setTo(to);
            } else {
                System.out.println("Open chat-room: Yes - y, No - n.");
                String markerCr = scanner.nextLine();
                if (markerCr.equals("y")) {
                    System.out.println("Enter the name of the chat-room: AAA, BBB, CCC, DDD.");
                    cr = scanner.nextLine();
                    ChatRoomGetThread.setCrName(cr);
                }
            }
            if (!to.equals("")) {
                Thread thPrivate = new Thread(new PrivateGetThread());
                thPrivate.setDaemon(true);
                thPrivate.start();
            } else {
                if (!cr.equals("")) {
                    Thread thCR = new Thread(new ChatRoomGetThread());
                    thCR.setDaemon(true);
                    thCR.start();
                } else {
                    Thread th = new Thread(new GetThread());
                    th.setDaemon(true);
                    th.start();
                }

            }

            System.out.println("Enter your message: ");
            while (true) {
                String text = scanner.nextLine();
                if (text.isEmpty()) {
                    Message mOff = new Message(login, text, password, to, cr, false);
                    int resOff = 0;
                    if (!to.equals("")) {
                        resOff = mOff.send(Utils.getURL() + "/addprivate");
                    } else {
                        if (!cr.equals("")) {
                            resOff = mOff.send(Utils.getURL() + "/addchatroom");
                        } else {
                            resOff = mOff.send(Utils.getURL() + "/add");
                        }
                    }
                    break;
                }

                Message m = new Message(login, text, password, to, cr, true);
                int res = 0;
                if (!to.equals("")) {
                    res = m.send(Utils.getURL() + "/addprivate");
                } else {
                    if (!cr.equals("")) {
                        res = m.send(Utils.getURL() + "/addchatroom");
                    } else {
                        res = m.send(Utils.getURL() + "/add");
                    }
                }
                if (res != 200) { // 200 OK
                    System.out.println("HTTP error occurred: " + res);
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
