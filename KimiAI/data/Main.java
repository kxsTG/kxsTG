package data;

public class Main {
    public static void main(String[] args) {
        Kimi kimi = Kimi.getKimi();
        System.out.println(kimi.request(Kimi.KIMI_LATEST,new Message[]{new Message(Roles.USER,
                ""
        )},0.3));
    }
}