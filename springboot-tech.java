
    @Autowired
    private ConsumerTokenServices tokenServices;

    @Override
    public boolean revokeToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        int bearLen = "bearer".length();

        if (authorization != null && authorization.length() > bearLen) {
            String bearer = authorization.substring(0, bearLen);
            if("bearer".equalsIgnoreCase(bearer)) {
                String accessToken = (authorization.substring(bearLen)).trim();
                return tokenServices.revokeToken(accessToken);
            }
        }
        throw new SCAException(ErrorCode.INVALID_AUTHORIZATION_HEADER);
    }
