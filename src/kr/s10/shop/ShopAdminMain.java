package kr.s10.shop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShopAdminMain {
	private BufferedReader br;
	private ShopDAO dao;
	
	public ShopAdminMain() {
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			dao = new ShopDAO();
			//메뉴 호출
			callMenu();
			
		}catch(Exception e ) {
			e.printStackTrace();
		}finally {
			if(br!=null)try {br.close();}catch(IOException e) {}
		}
	}
	//메뉴
	public void callMenu()throws IOException{
		while(true) {
			System.out.print("1.상품 등록, 2.상품 목록 보기, 3.회원 목록, 4.구매 목록, 5.종료>");
			try {
				int no = Integer.parseInt(br.readLine());
				if(no == 1) { //상품 등록
					System.out.print("이름: ");
					String item_name = br.readLine();
					System.out.print("가격: ");
					int item_prcie = Integer.parseInt(br.readLine());
					
					dao.insertItem(item_name, item_prcie);
				}else if(no == 2) { //상품 목록 보기
					dao.selectItem();
				}else if(no == 3) { //회원 목록
					dao.selectCustomer();
				}else if(no == 4) { //구매 목록
					dao.selectOrder();
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
		new ShopAdminMain();
	}
}
