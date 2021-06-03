package control.manage_store.dbprocess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Store;
import model.manage_member.MemberInfo;
import assets.DBConnectionMgr;

public class StoreInfoProcess {

	public static boolean updatePassword(int storeId, String newPassword) {
		boolean flag = false;
		
		System.out.println(" >> "+String.valueOf(storeId)+" => "+newPassword);
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		DBConnectionMgr pool = null;
		
		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			int num = 0;
			// id, tel, mileage, age
			con = pool.getConnection();
			sql = "UPDATE store SET kiosk_password = ? WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newPassword);
			pstmt.setInt(2, storeId);
			
			
			if (pstmt.executeUpdate() == 1)
				flag = true;
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			pool.freeConnection(con, pstmt);
		}
		
		return flag;
	}
	public static boolean updatePrice(int storeId, int newPrice) {
		boolean flag = false;
		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		DBConnectionMgr pool = null;
		
		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			int num = 0;
			// id, tel, mileage, age
			con = pool.getConnection();
			sql = "UPDATE store SET krw_per_hour = ? WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, newPrice);
			pstmt.setInt(2, storeId);
			
			
			if (pstmt.executeUpdate() == 1)
				flag = true;
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			pool.freeConnection(con, pstmt);
		}
		
		return flag;
	}
	public static boolean checkPassword(String password, String name) {
		boolean flag = false;
		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		DBConnectionMgr pool = null;
		
		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			int num = 0;
			// id, tel, mileage, age
			con = pool.getConnection();
			sql = "select * FROM store WHERE store_name = ? and kiosk_password = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			flag = pstmt.executeQuery().next();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			pool.freeConnection(con, pstmt);
		}
		
		return flag;
	}
	public static Store checkStore(String exAddress, String inAddress) {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		DBConnectionMgr pool = null;
		int storeId = 0;
		int krwPerHour = 0;
		String storeName = "";
		String externalAddress = "";
		String internalAddress = "";
		String lscAddress = "";
		String kioskPassword = "";

		Store newStore = new Store();
		
		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			int num = 0;
			// id, tel, mileage, age
			con = pool.getConnection();
			sql = "select * from store WHERE internal_address = ? and external_address = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, inAddress);
			pstmt.setString(2, exAddress);
			rs = pstmt.executeQuery();
//			isExist  pstmt.executeQuery().next();
			while (rs.next()) {
				storeId = rs.getInt("id");
				storeName = rs.getString("store_name");
				externalAddress = rs.getString("external_address");
				internalAddress = rs.getString("internal_address");
				krwPerHour = rs.getInt("krw_per_hour");
				kioskPassword = rs.getString("kiosk_password");
				
				newStore.setExternalAddress(externalAddress);
				newStore.setInternalAddress(internalAddress);
				newStore.setKioskPassword(kioskPassword);
				newStore.setKrwPerHour(krwPerHour);
				newStore.setStoreId(storeId);
				newStore.setStoreName(storeName);
			}
			

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			pool.freeConnection(con, pstmt);
		}
		return newStore;
	}
	

	public static Store checkStoreOnlyEx(String exAddress) {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		DBConnectionMgr pool = null;
		int storeId = 0;
		int krwPerHour = 0;
		String storeName = "";
		String externalAddress = "";
		String internalAddress = "";
		String lscAddress = "";
		String kioskPassword = "";

		Store newStore = new Store();
		
		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			int num = 0;
			// id, tel, mileage, age
			con = pool.getConnection();
			sql = "select * from store WHERE external_address = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, exAddress);
			rs = pstmt.executeQuery();
//			isExist  pstmt.executeQuery().next();
			while (rs.next()) {
				storeId = rs.getInt("id");
				storeName = rs.getString("store_name");
				externalAddress = rs.getString("external_address");
				internalAddress = rs.getString("internal_address");
				krwPerHour = rs.getInt("krw_per_hour");
				kioskPassword = rs.getString("kiosk_password");
				
				newStore.setExternalAddress(externalAddress);
				newStore.setInternalAddress(internalAddress);
				newStore.setKioskPassword(kioskPassword);
				newStore.setKrwPerHour(krwPerHour);
				newStore.setStoreId(storeId);
				newStore.setStoreName(storeName);
			}
			

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			pool.freeConnection(con, pstmt);
		}
		return newStore;
	}
	
	// 회원 DB로 부터 값을 읽어온다
	public static void readMember(ArrayList<MemberInfo> list) {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		DBConnectionMgr pool = null;

		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			int num = 0;
			// id, tel, mileage, age
			con = pool.getConnection();
			sql = "select id,tel,mileage,age from member where id<>'admin'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				String tel = rs.getString("tel");
				String mileage = new Integer(rs.getInt("mileage")).toString();
				String age = new Integer(rs.getInt("age")).toString();
				list.add(new MemberInfo(id, tel, mileage, age));
				num++;

			}
			if (num == 0) {
				JOptionPane.showMessageDialog(null, "조회할 데이터가 존재 하지 않습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			pool.freeConnection(con, pstmt);
		}

	}
	// 회원DB에서 값읽어 오는 메소드 종료
	
	
	//특정 회원 정보 불러온다.
	public static MemberInfo readPerson(String id) {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		DBConnectionMgr pool = null;
		MemberInfo memberInfo=null;
		
		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			int num = 0;
			// id, tel, mileage, age
			con = pool.getConnection();
			sql = "select id,tel,mileage,age from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String id2 = rs.getString("id");
				String tel = rs.getString("tel");
				String mileage = new Integer(rs.getInt("mileage")).toString();
				String age = new Integer(rs.getInt("age")).toString();
				memberInfo = new MemberInfo(id2, tel, mileage, age);
				num++;
			}
			if (num == 0) {
				JOptionPane.showMessageDialog(null, "조회할 데이터가 존재 하지 않습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return memberInfo;

	}
	// 회원DB에서 값읽어 오는 메소드 종료

	//특정 회원 정보 불러온다.
	public static int remainSecond(String id) {

		int remainSecond = 0;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		DBConnectionMgr pool = null;
		MemberInfo memberInfo=null;
		
		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			int num = 0;
			// id, tel, mileage, age
			con = pool.getConnection();
			sql = "select id,tel,mileage,age, remainSecond from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String id2 = rs.getString("id");
				String tel = rs.getString("tel");
				String mileage = new Integer(rs.getInt("mileage")).toString();
				String age = new Integer(rs.getInt("age")).toString();
				remainSecond = new Integer(rs.getInt("remainSecond")); 
				memberInfo = new MemberInfo(id2, tel, mileage, age);
				num++;
			}
			if (num == 0) {
				JOptionPane.showMessageDialog(null, "조회할 데이터가 존재 하지 않습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return remainSecond;

	}
	// 회원DB에서 값읽어 오는 메소드 종료
	

}
