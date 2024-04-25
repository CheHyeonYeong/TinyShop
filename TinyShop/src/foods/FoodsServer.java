package foods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class FoodsServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        FoodsDAO foodsDAO = new FoodsDAO();

        try {
            serverSocket = new ServerSocket(3018);
            System.out.println("연결을 기다리고 있습니다.");
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("클라이언트와 연결 성공");

            String msg;
            while ((msg = in.readLine()) != null) {
                String[] tokens = msg.split(",");
                String command = tokens[0];

                switch (command) {
                    case "getAllFoods":
                        String[] foodsList = foodsDAO.getAllFoods();
                        out.println(String.join(",", foodsList)); // 음식 목록을 쉼표로 구분하여 클라이언트에 전송
                        break;
                    case "addFood":
                        if (tokens.length == 5) {
                            FoodsVO newFood = new FoodsVO(tokens[1], tokens[2], tokens[3], Long.parseLong(tokens[4]));
                            int result = foodsDAO.insertFood(newFood);
                            if (result > 0) {
                                out.println("음식이 추가되었습니다.");
                            } else {
                                out.println("음식 추가에 실패했습니다.");
                            }
                        } else {
                            out.println("잘못된 요청입니다.");
                        }
                        break;
                    case "updateFood":
                        if (tokens.length == 6) {
                            try {
                                FoodsVO foodToUpdate = new FoodsVO(Integer.parseInt(tokens[1]), tokens[2], tokens[3], tokens[4], Long.parseLong(tokens[5]));
                                int result = foodsDAO.updateFood(foodToUpdate);
                                if (result > 0) {
                                    out.println("음식 정보가 업데이트 되었습니다.");
                                } else {
                                    out.println("음식 정보 업데이트에 실패했습니다.");
                                }
                            } catch (NumberFormatException e) {
                                out.println("잘못된 요청입니다. 숫자 형식이 올바르지 않습니다.");
                            }
                        }
                        break;
                    case "deleteFood":
                        if (tokens.length == 2) {
                            int foodId = Integer.parseInt(tokens[1]);
                            int result = foodsDAO.deleteFood(foodId);
                            if (result > 0) {
                                out.println("음식이 삭제되었습니다.");
                            } else {
                                out.println("음식 삭제에 실패했습니다.");
                            }
                        } else {
                            out.println("잘못된 요청입니다.");
                        }
                        break;
                    default:
                        out.println("알 수 없는 명령입니다.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}