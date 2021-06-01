package control.manage_member.dbprocess;

import java.sql.Connection;
import java.sql.PreparedStatement;


import assets.DBConnectionMgr;

public class JoinMemberProcess {

	public static boolean findMember(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int flag = 0; // 0은 실패, 1=회원가입 성공, 2=아이디가 중복됨
		DBConnectionMgr pool = null;

		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}


		boolean flag2 = false;
		try {
			con = pool.getConnection();
			sql = "select id from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			flag2 = pstmt.executeQuery().next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag2;

	}
	
	public static int insertMember(String id, String pass, String tel, String age) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int flag = 0; // 0은 실패, 1=회원가입 성공, 2=아이디가 중복됨
		DBConnectionMgr pool = null;

		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}


		boolean flag2 = false;
		try {
			con = pool.getConnection();
			sql = "select id from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			flag2 = pstmt.executeQuery().next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(flag2 == true) {
			pool.freeConnection(con, pstmt);
			return 2;
		}
		else {
			try {
	
				// num, id, password, tel, mileage, age
				con = pool.getConnection();
				sql = "insert member(id, password, tel, mileage, age )"
						+ "values(?,?,?,?,?)";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, pass);
				pstmt.setString(3, tel);
				pstmt.setInt(4, 0);
				pstmt.setString(5, age);
	
				if (pstmt.executeUpdate() == 1)
					flag = 1;
	
			} catch (Exception e) {
				e.printStackTrace();
	
			} finally {
				pool.freeConnection(con, pstmt);
			}
			return flag;
		}
	}

}
