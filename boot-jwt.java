
	public User findById(int id) {
		Optional<User> userOptional = userRepository.findById(id);
		if(userOptional.isPresent()) {
		    return userOptional.get();
		}
		return null;
	}
  
	public void delete(int id) {
        Optional<User> userOptional = userRepository.findById(id);
		if(userOptional.isPresent()) {
		    userRepository.delete(userOptional.get());
		}
	}
  
  ===
  http.csrf().disable()
		    .anonymous().and()
            .authorizeRequests()
            .antMatchers("/login",
                    "/logout",
                    "/v2/api-docs",
                    "/configuration/ui",
                    "/swagger-resources/**",
                    "/configuration/security",
                    "/swagger-ui.html",
                    "/webjars/**").permitAll();

		//restServicesEntryPoint() sẽ xử lý những request chưa được xác thực.
//		http.authorizeRequests().antMatchers("/rest/**").authenticated().and()

		//Các url /rest/** với method GET (API lấy thông tin user) 
				//cho phép cả role ADMIN và USER truy cập, với các method 
				//“DELETE” và “POST” (xóa và tạo mới user) thì chỉ cho phép role ADMIN truy cập.
		http.authorizeRequests()
//		    .antMatchers(HttpMethod.GET, "/api/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//			.antMatchers(HttpMethod.POST, "/api/**").access("hasRole('ROLE_ADMIN')")
//			.antMatchers(HttpMethod.DELETE, "/api/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().authenticated();
