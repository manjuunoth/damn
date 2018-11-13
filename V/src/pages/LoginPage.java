package pages;

import org.testng.asserts.SoftAssert;
import org.xframium.page.Page;

public interface LoginPage extends Page 
{

	@ElementDefinition
	public static final String txt_usernameFP = "txt_usernameFP";
	
	@ElementDefinition
	public static final String txt_passwordFP = "txt_passwordFP";
	
	@ElementDefinition
	public static final String btn_signinFP = "btn_signinFP";
	
	@ElementDefinition
	public static final String btn_signoffFP ="btn_signoffFP";

	@TimeMethod
	@ScreenShot
	public void login(String tcID,SoftAssert softAssert);
	
	public void logout(String tcID,SoftAssert softAssert);
	
}
