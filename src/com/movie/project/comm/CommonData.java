package com.movie.project.comm;

public class CommonData {
	public static String server_ip = "";

	public static String SERVER_ADDRESS = "http://192.168.4.252:8081/";
	public static String IMAGE_ADDRESS = ".scar.com.cn/upload/fuwu/";

	public static final int HTTP_HANDLE_SUCCESS = 1;
	public static final int HTTP_HANDLE_FAILE = 0;
	public static final int HTTP_TIME_OUT = 1010;
	//毫秒
	public static final int TIME_OUT = 6000;
	
	public static final String USER_NAME="userName";
	public static final String USER_ID="userId";
	public static final String NICK_NAME="nickName";
	public static final String USER_SEX="userSex";
	public static final String USER_PWD="userPwd";
	public static final String USER_PHONE="userPhone";
	public static final String USER_EMAIL="userEmail";
	public static final String USER_CITY="userCity";
	public static final String Bland1="bland1";
	public static final String Bland2="bland2";
	public static final String Bland3="bland3";
	public static final String BlandId1="blandId1";
	public static final String BlandId2="blandId2";
	public static final String BlandId3="blandId3";
	
	/**
	 * 一页数量
	 */
	public static final int pageSize = 100;
	//已支付
	public static final String PAY = "0";
	//已消费
	public static final String COUSUME = "1";
	public static final String TIME = "2";
	//全部
	public static final String TIME_ALL = "0";
	//一个月
	public static final String TIME_MONTH = "1";
	//一个星期
	public static final String TIME_WEEk = "2";
	public static int currentPage;
	
	
	
	
	
	
	/**
	 * 排序方式
	 */
	public static final int sort_type_1 = 0;// 0：默认；1：最新上架；2：价格最低；3：价格最高
	public static final int sort_type_2 = 1;
	public static final int sort_type_3 = 2;
	public static final int sort_type_4 = 3;
	
}
