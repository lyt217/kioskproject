package _client.dbprocess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import _client.assets.DBConnectionMgr;
import model.Computer;

public class GetComputer {

	// 가격 가져오는 메소드 시작
	public static Computer getComputer(String internalAddress, int storeId) {
		int price=0;
		DBConnectionMgr pool = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean flag = false;
		

		Computer computer = new Computer();
		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			con = pool.getConnection();
			sql = "select * from computers where internalAddress = ? and storeId = ? and in_use = 0";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, internalAddress);
			pstmt.setInt(2, storeId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int seatNumber = rs.getInt("seatNumber");
				int id = rs.getInt("id");
				int store_id = rs.getInt("storeId");
				String internal_address = rs.getString("internalAddress");
				
				computer.setId(id);
				computer.setStoreId(store_id);
				computer.setInternalAddress(internal_address);
				computer.setSeatNumber(seatNumber);
			}

			String sqlu = "UPDATE computers SET in_use = 1 WHERE id = ?";
			pstmt = con.prepareStatement(sqlu);
			pstmt.setInt(1, computer.getId());
			if (pstmt.executeUpdate() == 1){

			} 
			else{
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}

		return computer;
	}
	// 가격 가져오는 메소드 종료

}
