package pages;

import org.testng.asserts.SoftAssert;
import org.xframium.page.Page;

public interface LoginPage extends Page 
{
	
	
	@ElementDefinition
	public static final String icon_LoginM = "icon_LoginM";
	
	@ElementDefinition
	public static final String txt_usernameMMT = "txt_usernameMMT";
	
	@ElementDefinition
	public static final String txt_passwordMMT = "txt_passwordMMT";
	
	@ElementDefinition
	public static final String btn_signinMMT = "btn_signinMMT";
	
	@ElementDefinition
	public static final String btn_logo ="btn_logo";
	
	@ElementDefinition
	public static final String btn_signoffFP ="btn_signoffFP";
	
	@ElementDefinition
	public static final String  btn_womenMenu="btn_womenMenu";

	@TimeMethod
	@ScreenShot
	public void login(String tcID,SoftAssert softAssert);
	
	public void logout(String tcID,SoftAssert softAssert);
	
}
