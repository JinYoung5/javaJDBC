package kr.s10.shop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ShopUserMain {
	private BufferedReader br;
	private ShopDAO dao;
	
	public ShopUserMain() {
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			dao = new ShopDAO();
			//메뉴 호출
			callMenu();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(br!=null)try {br.close();}catch(IOException e) {}	
		}
	}
	//메뉴
	public void callMenu()throws IOException{
		while(true) {
			System.out.print("1.회원 가입, 2회원 정보, 3.상품 구매, 4.구매 내역, 5.종료>");
			try {
				int no = Integer.parseInt(br.readLine());
				if(no == 1) { //회원 가입
					System.out.print("아이디: ");
					String cust_id = br.readLine();
					System.out.print("이름: ");
					String cust_name = br.readLine();
					System.out.print("주소: ");
					String cust_address = br.readLine();
					System.out.print("전화번호: ");
					String cust_tel = br.readLine();
					
					dao.insertCustomer (cust_id, cust_name, cust_address, cust_tel);
				}else if(no == 2) { //회원 정보
					System.out.print("아이디: ");
					String cust_id = br.readLine();
					
					dao.selectDetailCustomer(cust_id);
				}else if(no == 3) { //상품 구매
					dao.selectItem();
					System.out.println("상품 구매를 시작합니다.");
					System.out.println("-------------------------------");
					System.out.print("상품 번호: ");
					int item_num = Integer.parseInt(br.readLine());
					System.out.print("아이디: ");
					String cust_id = br.readLine();
					
					dao.insertOrder(item_num, cust_id);
				}else if(no == 4) { //구매 내역
					 System.out.print("아이디: ");
					 String cust_id = br.readLine();
					 
					 dao.selectOrderById(cust_id);
				}else if(no == 5) { //종료
					System.out.println("프로그램 종료");
					break;
				}else {
					System.out.println("잘못 입력했습니다.");
				}
			}catch(NumberFormatException e) {
				System.out.println("[숫자만 입력 가능]");
			}
		}
	}
	
	public static void main(String[] args) {
		new ShopUserMain();
	}
}
