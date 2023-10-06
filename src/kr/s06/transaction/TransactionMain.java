package kr.s06.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import kr.util.DBUtil;

public class TransactionMain {
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		String sql = null;
		
		try {
			//JDBC 수행 1,2단계
			conn = DBUtil.getConnection();
			
			//트랜잭션을 수동 처리하기 위해 auto commit 해체
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO test1 VALUES('Seoul',10)";
			pstmt1 = conn.prepareStatement(sql);
			pstmt1.executeUpdate();
			
			sql = "INSERT INTO test1 VALUES ('Busan',20)";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.executeUpdate();
			
			sql = "INSERT INTO test1 VALUES ('Jeju',30";
			pstmt3 = conn.prepareStatement(sql);
			pstmt3.executeUpdate();
			
			//정상적으로 작업이 완료되면 commit
			conn.commit();
			
			System.out.println("작업 완료!!");
			
		}catch(Exception e) {
			e.printStackTrace();
			//예외가 발생했을 경우 rollback
			try {
				conn.rollback();
			}catch(SQLException se) {
				se.printStackTrace();
			}
		}finally {
			//자원 정리
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt1, null);
			
		}
		
	}
}
