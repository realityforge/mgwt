package com.googlecode.mgwt.linker.server.propertyprovider.test;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.mgwt.linker.server.propertyprovider.MgwtOsPropertyProvider;
import static org.mockito.Mockito.*;

public class TestMgwtOsPropertyProvider {

	private MgwtOsPropertyProvider provider;

	@Before
	public void setUp() throws Exception {

		provider = new MgwtOsPropertyProvider();
	}

	@Test
	public void testGetPropertyName() {
		Assert.assertEquals("mgwt.os", provider.getPropertyName());
	}

	@Test
	public void testGetPropertyValueIphoneWithoutScreenCookie() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.IPHONE_IOS5_USER_AGENT );
        when( mockServletRequest.getCookies() ).thenReturn( new Cookie[0] );

		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("iphone_undefined", propertyValue);

	}

	@Test
	public void testGetPropertyValueIphoneScreenCookieRetina() {
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie("mgwt_ios_retina", "1");
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.IPHONE_IOS5_USER_AGENT );
        when( mockServletRequest.getCookies() ).thenReturn( cookies );
		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("retina", propertyValue);

	}

	@Test
	public void testGetPropertyValueIphoneScreenCookieNonRetina() {
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie("mgwt_ios_retina", "0");
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.IPHONE_IOS5_USER_AGENT );
        when( mockServletRequest.getCookies() ).thenReturn( cookies );
		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("iphone", propertyValue);

	}

	@Test
	public void testGetPropertyValueIpad() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.IPAD_IOS5_USER_AGENT );

		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("ipad_undefined", propertyValue);

	}

	@Test
	public void testGetPropertyValueIpadNonRetina() {
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie("mgwt_ios_retina", "0");
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.IPAD_IOS5_USER_AGENT );
        when( mockServletRequest.getCookies() ).thenReturn( cookies );

		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("ipad", propertyValue);

	}

	@Test
	public void testGetPropertyValueIpadRetina() {
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie("mgwt_ios_retina", "1");
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.IPAD_IOS5_USER_AGENT );
        when( mockServletRequest.getCookies() ).thenReturn( cookies );

		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("ipad_retina", propertyValue);

	}

	@Test
	public void testGetPropertyValueAndroid() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.ANDROID_PHONE_2x_USER_AGENT );

        String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("android", propertyValue);

	}

	@Test
	public void testGetPropertyValueAndroidTablet() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.ANDROID_TABLET_USER_AGENT );

		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("android_tablet", propertyValue);

	}

	@Test
	public void testGetPropertyValueDesktopChrome() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.DESKTOP_USER_AGENT_CHROME );

		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("desktop", propertyValue);

	}

	@Test
	public void testGetPropertyValueDesktopSafari() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.DESKTOP_USER_AGENT_SAFARI );

		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("desktop", propertyValue);

	}

	@Test
	public void testGetPropertyValueDesktopFirefox() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.DESKTOP_USER_AGENT_FIREFOX );

		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("desktop", propertyValue);

	}

	@Test
	public void testGetPropertyValueBlackberry() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( UserAgents.BLACKBERRY_USER_AGENT );

		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("blackberry", propertyValue);

	}

	@Test
	public void testGetPropertyValueDesktopAsDefault() {
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getHeader( "User-Agent" ) ).thenReturn( "" );

		String propertyValue = provider.getPropertyValue(mockServletRequest);

		Assert.assertEquals("desktop", propertyValue);

	}

	@Test
	public void testGetRetinaCookieValueValid() {
		Cookie[] cookies = new Cookie[2];
		cookies[0] = new Cookie("test", "bla");
		cookies[1] = new Cookie("mgwt_ios_retina", "bla");

        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getCookies() ).thenReturn( cookies );

		String retinaCookieValue = provider.getRetinaCookieValue(mockServletRequest);

		Assert.assertEquals("bla", retinaCookieValue);
	}

	@Test
	public void testGetRetinaCookieValueInvalid() {
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie("test", "bla");
        HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );
        when( mockServletRequest.getCookies() ).thenReturn( cookies );

		String retinaCookieValue = provider.getRetinaCookieValue(mockServletRequest);

		Assert.assertNull(retinaCookieValue);
	}

	@Test
	public void testGetRetinaCookieValueWithoutCookiesValues() {
		HttpServletRequest mockServletRequest = mock( HttpServletRequest.class );

		String retinaCookieValue = provider.getRetinaCookieValue(mockServletRequest);

		Assert.assertNull(retinaCookieValue);
	}

}
