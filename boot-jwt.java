
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        //bean jwtAuthenticationTokenFilter sẽ thực hiện việc xác thực người dùng
        // Nếu ko disable csrf thì trên swagger sẽ báo lỗi:
	    // Could not verify the provided CSRF token because your session was not found
		http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/login",
                    "/logout",
                    "/v2/api-docs",
                    "/configuration/ui",
                    "/swagger-resources/**",
                    "/configuration/security",
                    "/swagger-ui.html",
                    "/webjars/**").permitAll()
//		    .antMatchers(HttpMethod.GET, "/api/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//			.antMatchers(HttpMethod.POST, "/api/**").access("hasRole('ROLE_ADMIN')")
//			.antMatchers(HttpMethod.DELETE, "/api/**").access("hasRole('ROLE_ADMIN')")
		    .anyRequest().authenticated().and()
		    .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		    .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());

	}
