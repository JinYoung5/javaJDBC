package kr.s03.preparedstatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import kr.util.DBUtil;

public class SelectTestMain {
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;;
		
		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			
			//SQL문 작성
			sql = "SELECT * FROM test1 ORDER BY id ASC";
			
			//JDBC 수행 3 단계 : PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//JDBC 수행 4 단계 : SQL문을 실행해서 결과행들을 ResultSet에 담음
			rs = pstmt.executeQuery();
			System.out.println("ID\t나이");
			while(rs.next()) {
				System.out.print(rs.getString("id"));
				System.out.print("\t");
				System.out.println(rs.getInt("age"));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			//자원 정리
			 DBUtil.executeClose(rs, pstmt, conn);
		}
	}
}
