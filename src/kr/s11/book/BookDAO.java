package kr.s11.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;

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
			System.out.println(count + "건의 도서 정보를 등록했습니다.");
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
			sql = "SELECT * FROM book ORDER BY bk_num DESC";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//JDBC 수행 4단계
			rs = pstmt.executeQuery();

			/*if(int bk_num == 0) {
				re_status
				"대출가능"
				
			}else if(bk_num == 1) {
				re_status
				"대출중"
			}
			*/
					
			System.out.println("-----------------------------------------");
			if(rs.next()) {

				System.out.println("번호\t카테고리\t도서명\t대출여부\t등록일");
				do {
					System.out.print(rs.getInt("bk_num") + "\t");
					System.out.print(rs.getString("bk_category") + "\t");
					System.out.print(rs.getString("bk_name") + "\t");
					System.out.print(rs.getInt("re_status") + "\t");
					System.out.print(rs.getDate("bk_date"));

				}while(rs.next());
			}else {
				System.out.println("등록된 책이 없습니다.");
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
			sql = "SEELCT * FROM member ORDER BY me_date DESC";
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
					System.out.println(rs.getDate("me_date"));
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
	
	//사용자 도서 대출 등록
	public void insertReservation(String bk_name, String bk_category) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "INSERT INTO reservation (bk_num, bk_name, bk_category)" + "VALUES (bk_seq.nextval,?,?";
			//JDBC 수행 3단계
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, bk_name);
			pstmt.setString(2, bk_category);
			//JDBC 수행 4단계
			int count = pstmt.executeUpdate();
			System.out.println("도서 " + count + "건이 대출되었습니다.");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//사용자 MY 대출 목록 보기(현재 대출한 목록만 표시)
	//사용자 도서 반납 가능 여부(대출번호(re_num)과 회원 id(me_id)를 함께 조회해서 re_status가 1인 것은 반납, 0이면 반납 불가능
	//사용자 반납 처리
}
