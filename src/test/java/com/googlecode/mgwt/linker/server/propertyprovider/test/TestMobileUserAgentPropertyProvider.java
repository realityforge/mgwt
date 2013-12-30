package com.googlecode.mgwt.linker.server.propertyprovider.test;

import javax.servlet.http.HttpServletRequest;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.mgwt.linker.server.propertyprovider.MobileUserAgentProvider;
import static org.mockito.Mockito.*;

public class TestMobileUserAgentPropertyProvider {

	private MobileUserAgentProvider provider;

	@Before
	public void setUp() throws Exception {
		provider = new MobileUserAgentProvider();
	}

	@Test
	public void testGetPropertyName() {
		Assert.assertEquals("mobile.user.agent", provider.getPropertyName());
	}

	@Test
	public void testGetPropertyValueIphone() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.IPHONE_IOS5_USER_AGENT );
		Assert.assertEquals("mobilesafari", provider.getPropertyValue(mockServletRequest));
	}

	@Test
	public void testGetPropertyValueIpad() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.IPAD_IOS5_USER_AGENT );
		Assert.assertEquals("mobilesafari", provider.getPropertyValue(mockServletRequest));
	}

	@Test
	public void testGetPropertyValueAndroid() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.ANDROID_PHONE_2x_USER_AGENT );
		Assert.assertEquals("mobilesafari", provider.getPropertyValue(mockServletRequest));
	}

	@Test
	public void testGetPropertyValueBlackberry() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.BLACKBERRY_USER_AGENT );
		Assert.assertEquals("not_mobile", provider.getPropertyValue(mockServletRequest));
	}

	@Test
	public void testGetPropertyValueDesktop() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.DESKTOP_USER_AGENT_CHROME );
		Assert.assertEquals("not_mobile", provider.getPropertyValue(mockServletRequest));
	}

}
