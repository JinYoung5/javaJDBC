package kr.s11.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import kr.util.DBUtil;

/* 순서
1.관리자 도서 등록
2.관리자 도서 목록
3.사용자 회원 가입
4.관리자 회원 목록
5.사용자 도서 대출
6.관리자 대출 목록
7.사용자 MY대출 목록
8.사용자 대출 도서 반납
 */
public class BookDAO {
	//관리자 도서 등록
	public void insertBook(String bk_category, String bk_name) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "INSERT INTO book (bk_num, bk_category, bk_name)" + "VALUES (book_seq.nextval,?,?)";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, bk_category);
			pstmt.setString(2, bk_name);
			//JDBC 수행 4단계
			int count = pstmt.executeUpdate();
			System.out.println(count + "권의 도서 정보를 등록했습니다.");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//관리자 도서 목록
	public void selectBook() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT bk_num, bk_category, bk_name, re_status, bk_regdate FROM book LEFT OUTER JOIN (SELECT * FROM reservation WHERE re_status = 1) USING(bk_num) ORDER BY bk_num DESC";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//JDBC 수행 4단계
			rs = pstmt.executeQuery();
	
			System.out.println("-----------------------------------------");
			if(rs.next()) {

				System.out.println("번호\t카테고리\t도서명\t대출여부\t등록일");
				do {
					System.out.print(rs.getInt("bk_num") + "\t");
					System.out.print(rs.getString("bk_category") + "\t");
					System.out.print(rs.getString("bk_name") + "\t");
					System.out.print(rs.getInt("re_status") + "\t");
					if(rs.getInt("re_status")==0) {
						System.out.print("대출가능" + "\t");
					}else {
						System.out.print("대출중" + "\t");
					}
					System.out.print(rs.getDate("bk_regdate"));

				}while(rs.next());
			}else {
				System.out.println("등록된 도서가 없습니다.");
			}
			System.out.println("-----------------------------------------");
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}
	//관리자 회원 목록
	public void selectMember() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SEELCT me_id, me_name, me_phone, me_regdate FROM member ORDER BY me_regdate DESC";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//JDBC 수행 4단계
			rs = pstmt.executeQuery();
			System.out.println("---------------------------------------");
			if(rs.next()) {
				System.out.println("아이디\t이름\t전화번호\t가입일");
				do {
					System.out.print(rs.getString("me_id") + "\t");
					System.out.print(rs.getString("me_name") + "\t");
					System.out.print(rs.getString("me_phone") + "\t");
					System.out.println(rs.getDate("me_regdate"));
				}while(rs.next());
			}else {
				System.out.println("등록된 회원 정보가 없습니다.");
			}
			System.out.println("---------------------------------------");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
	}
	//관리자 대출 목록 보기(대출 및 반납 - 모든 데이터 표시)
	public void selectReservation() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;

		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT re_num, re_status, me_id, bk_name, re_regdate, re_modifydate FROM reservation JOIN book USING(bk_num) ORDER BY re_num DESC";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//JDBC 수행 4단계
			System.out.println("------------------------------------------------");
			if(rs.next()) {
				System.out.println("번호\t대출여부\t아이디\t대출도서명\t대출일\t반납일");
				System.out.println("------------------------------------------------");
				do {
					System.out.print(rs.getInt("re_num") + "\t");
					if(rs.getInt("re_status")==0) {
						System.out.print("반납" + "\t");
					}else {
						System.out.print("대출" + "\t");
					}
					System.out.print(rs.getInt("re_status") + "\t");
					System.out.print(rs.getString("me_id") + "\t");
					System.out.print(rs.getString("bk_book") + "\t");
					System.out.print(rs.getDate("re_regdate") + "\t");
					if(rs.getDate("re_modifydate") == null) {
						System.out.println("");
					}else {					
						System.out.println(rs.getDate("re_modifydate"));
					}
				}while(rs.next());
			}else {
				System.out.println("등록된 도서 정보가 없습니다.");
			}
			System.out.println("------------------------------------------------");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}

	}
	//사용자 아이디 중복 체크(ID중복시 1, 미중복시 0)
	public int checkId(String me_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;

		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT me_id FROM member WHERE me_id=?";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, me_id);
			//JDBC 수행 4단계
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = 1;
			}
		}catch(Exception e) {
			e.printStackTrace();	
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	//사용자 회원 가입
	public void insertMember(String me_id, String me_passwd, String me_name, String me_phone) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "INSERT INTO member(me_id, me_passwd, me_name, me_phone)" + "VALUES(?,?,?,?)";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, me_id);
			pstmt.setString(2, me_passwd);
			pstmt.setString(3, me_name);
			pstmt.setString(4, me_phone);
			//JDBC 수행 4단계
			int count = pstmt.executeUpdate();
			System.out.println(me_name + "님의 회원 가입이 완료 되었습니다.");
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//사용자 로그인 체크 (로그인 성공 true, 로그인 실패 false 반환)
	public boolean loginCheck(String me_id, String me_passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean flag = false;

		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT me_id FROM member WHERE me_id=? AND me_passwd=?";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, me_id);
			pstmt.setString(2, me_passwd);
			//JDBC 수행 4단계
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				flag = true; //로그인 성공
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return flag;
	}
	//사용자 도서 대출 여부 확인(도서번호(bk_num)로 검색해서 re_status의 값이 0이면 대출가능, 1이면 대출 불가능)
	public int checkRent(int bk_num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;
		
		try {
			//JDBC수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT re_status FROM book LEFT OUTER JOIN (SELECT * FROM reservation WHERE re_status = 1) USING(bk_num) WHERE bk_num = ?";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setInt(1, bk_num);
			//JDBC 수행 4단계
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt("re_status");
			}else {
				count = 0;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	//사용자 도서 대출 등록
	public void insertBookReservation(int bk_num, String me_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "INSERT INTO reservation (re_num, bk_num, me_id, re_status)" + "VALUES (reservation_seq.nextval,?,?,1)";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setInt(1, bk_num);
			pstmt.setString(2, me_id);
			//JDBC 수행 4단계
			int count = pstmt.executeUpdate();
			System.out.println("도서 " + count + "권이 대출되었습니다.");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//사용자 MY 대출 목록 보기(현재 대출한 목록만 표시)
	public void selectmyReservation(String me_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT re_num, bk_name, re_status, re_regdate FROM reservation JOIN book USING(bk_num) WHERE me_id = ? AND re_status = 1";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, me_id);
			//JDBC 수행 4단계
			System.out.println("-----------------------------------------");
			if(rs.next()) {
				System.out.println("번호\t대출도서명\t대출여부\t등록일");
				System.out.println("-----------------------------------------");
				do {
					System.out.print(rs.getInt("re_num") + "\t");
					System.out.print(rs.getString("bk_name") + "\t");
					if(rs.getInt("re_status") == 1) {
						System.out.println("대출중" + "\t");
					}
					System.out.print(rs.getInt("re_status") + "\t");
					System.out.println(rs.getDate("re_regdate"));
				}while(rs.next());
			}else {
				System.out.println("등록된 MY대출 목록이 없습니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}
	//사용자 도서 반납 가능 여부(대출번호(re_num)과 회원 id(me_id)를 함께 조회해서 re_status가 1인 것은 반납, 0이면 반납 불가능
	public int returnCheck(int re_num, String me_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;
		
		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT re_status FROM book LEFT OUTER JOIN (SELECT * FROM reservation WHERE re_status = 1) USING(bk_num) WHERE re_num = ? AND me_id = ?";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setInt(1, re_num);
			pstmt.setString(2, me_id);
			//JDBC 수행 4단계
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = 1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	//사용자 반납 처리
	public void updateReservation(String me_id, int re_num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "UPDATE reservation SET re_status = 0, re_modifydate = SYSDATE WHERE me_id = ? AND re_num = ?";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, me_id);
			pstmt.setInt(2, re_num);
			//JDBC 수행 4단계
			int count = pstmt.executeUpdate();
			System.out.println("도서" + count + "권의 도서가 반납 되었습니다.");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}
}
