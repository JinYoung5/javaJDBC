package kr.s11.book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BookUserMain {
	private BufferedReader br;
	private BookDAO dao;
	private String me_id; //로그인한 아이디 저장
	private boolean flag; //로그인 여부
	
	public BookUserMain() {
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			dao = new BookDAO();
			//메뉴 호출
			callMenu();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(br!=null)try {br.close();}catch(IOException e) {}
		}
	}
	//메뉴
	public void callMenu() throws IOException{
		while(true) {
			System.out.print("1.로그인, 2.회원가입, 3.종료:");
			try {
				int no = Integer.parseInt(br.readLine());
				if(no == 1) { // 로그인
					System.out.print("아이디: ");
					me_id = br.readLine();
					System.out.print("비밀번호: ");
					String me_passwd = br.readLine();
					
					flag = dao.loginCheck(me_id, me_passwd);
					if(flag) {
						System.out.println(me_id + "님 로그인 되었습니다.");
						break;
					}
					System.out.println("아이디 또는 비밀번호 불일치");
				}else if(no == 2) { //회원가입
					System.out.print("아이디: ");
					String me_id = br.readLine();
					
					//아이디 중복 체크
					int check = dao.checkId(me_id);
					if(check == 1){
						System.out.println("아이디가 중복 되었습니다.");
					}else {
						
						/*
						 * 비밀번호 : 1234
						 * 이름 :홍길동
						 * 전화번호 : 010-1234-5678
						 * 회원 가입이 완료되었습니다
						 */
					}
					
					System.out.print("비밀번호: ");
					String me_passwd = br.readLine();
					System.out.print("이름: ");
					String me_name = br.readLine();
					System.out.print("전화번호: ");
					String me_phone = br.readLine();
					
					dao.insertMember(me_id, me_passwd, me_name, me_phone);

				}else if(no == 3) { //종료
					System.out.println("프로그램 종료");
					break;
				}else {
					System.out.println("잘못 입력했습니다.");
				}
			}catch(NumberFormatException e) {
				System.out.println("[숫자만 입력 가능]");
			}
		}
		while(flag) {
			System.out.print("1.도서대출, 2.MY대출목록, 3.대출도서 반납, 4.종료>");
			try {
				int no = Integer.parseInt(br.readLine());
				if(no == 1) { //도서 대출
					/*
					 * -----------------------------------------
					 * 번호	카테고리	도서명	대출여부	등록일	
					 * -----------------------------------------
					 * 43	IT		자바		대출가능	2023-10-10
					 * 42	천문		별이야기	대출가능	2023-10-10
					 * -----------------------------------------
					 * [도서 대출하기]
					 * 도서 번호: 43
					 * 도서 1건이 대출되었습니다. 
					 */
					dao.selectBook();
					System.out.println("[도서 대출하기]");
					System.out.print("도서 번호: ");
					int bk_num = Integer.parseInt(br. readLine());
					//System.out.print("아이디: ");
					//me_id = br.readLine();
			
					dao.insertBookReservation(bk_num, me_id);
				}else if(no == 2) { //MY대출목록
					/* 
					 * -----------------------------------------
					 * 번호	도서명	대출여부	등록일
					 * 41	자바		대출중	2023-10-10
					 * -----------------------------------------
					 */
					System.out.print("아이디: ");
					me_id = br.readLine();
					
					dao.selectmyReservation(me_id);
				}else if(no == 3) { //대출도서 반납
					/*
					 * -----------------------------------------
					 * 번호	도서명	대출여부	등록일
					 * 41	자바		대출중	2023-10-10
					 * -----------------------------------------
					 * [도서 반납하기]
					 * 대출번호: 41
					 * 1건의 도서가 반납되었습니다.
					 * 
					 */
					System.out.println("[도서 반납하기]");
					System.out.print("아이디: ");
					me_id = br.readLine();
					
					dao.selectmyReservation(me_id);
					
					System.out.print("대출번호: ");
					int re_num = Integer.parseInt(br.readLine());
					
					dao.updateReservation(me_id, re_num);
				}else if(no == 4) { //종료
					System.out.println("프로그램 종료");
					break;
				}else {
					System.out.println("잘못 입력 했습니다.");
				}
			}catch(NumberFormatException e) {
				System.out.println("[숫자만 입력 가능]");
				
			}
		}
	}
	public static void main(String[] args) {
		new BookUserMain();
		
	}
}
