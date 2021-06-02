package control.manage_member.dbprocess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import assets.DBConnectionMgr;
import model.manage_member.MemberInfo;

public class DepositMemberProcess {

	
	public static int insertMoney(String id, int money, int store_id) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = null, sql2 = null;
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
			
			String sql0 = "SELECT * FROM settings";
			pstmt = con.prepareStatement(sql0);
			rs = pstmt.executeQuery();
			int moneyPerSec = 100000000;
			while (rs.next()) {
				moneyPerSec = rs.getInt("money_per_hour");
			}
			
			int minute = money * 60 * 60 / moneyPerSec;
			
			sql = "UPDATE member SET remainSecond = remainSecond + ? WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, minute);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			
			sql2 = "INSERT INTO purchase (store_id, member_id, amount) VALUE (?, ?, ?)";
			pstmt = con.prepareStatement(sql2);
			pstmt.setInt(1, store_id);
			pstmt.setString(2, id);
			pstmt.setInt(3, money);
			pstmt.executeUpdate();
			
			flag2 = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(flag2 == true) {
			pool.freeConnection(con, pstmt);
			return 1;
		}
		else {
			return 0;
		}
	}

}
