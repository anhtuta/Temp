@Component
public class CustomTokenEnhancer implements TokenEnhancer {
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
			OAuth2Authentication authentication) {
	    Map<String, Object> details = new HashMap<>();
	    details.put("fullName", "Tạ Anh Tú");
	    details.put("address", "Hà Nội, Vietnam");
	    details.put("school", "Bách Khoa HN");
	    
		Map<String, Object> additionalInfo = new HashMap<>();
		additionalInfo.put("organization", authentication.getName());
		additionalInfo.put("Hehehe", "This is a demo!!!");
		additionalInfo.put("details", details);
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}
}
