public class BootTechAspect {

    /**
     * This checking is just for demo! Tất cả API có annotation RequireNonsense đều phải gửi kèm 1
     * header nonsense là 1 string bắt đầu = giá trị của thuộc tính prefix trong annotation @RequireNonsense.
     * Giá trị mặc định đó = 'tuzaku'!
     * Note: value = @annotation(requireNons) chứ ko phải @annotation(RequireNonsense) nhé!
     */
    @Before(value = "@annotation(requireNons)")
    public void checkNonsense(JoinPoint joinPoint, RequireNonsense requireNons) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String nonsense = request.getHeader("nonsense");
        if (StringUtils.isEmpty(nonsense)) {
            throw new RestException(StatusType.NONSENSE_IS_MISSING);
        }

        String prefix = requireNons.prefix();
        if (prefix.equals("")) {
            prefix = "tuzaku";
        }
        if (!nonsense.startsWith(prefix)) {
            throw new RestException(StatusType.INVALID_NONSENSE);
        }
    }

    /**
     * Định nghĩa pointcut: thực hiện pointcut với tất cả các method trong class BookController.
     * Nếu đổi thành "execution(* hello.controller.abc(..))" thì nó chỉ thực hiện với method abc
     */
    @Pointcut("execution(* hello.controller.BookController.*(..))")
    public void bookControllerPointcut() {
    }

    @Around("bookControllerPointcut()")
    public void printLog(ProceedingJoinPoint pjp)  {
        // Before advice
        long beforeRun = System.currentTimeMillis();

        try {
            // Thực hiện hàm bookControllerPointcut()
            pjp.proceed();

            // After Returning advice, you can do something here...
        } catch (Throwable e) {
            // After Throwing advice
            System.err.println("[Advice] Error: " + e.getMessage());
        }

        // after advice
        long afterRun = System.currentTimeMillis();
        System.out.println("Calling method: " + pjp.getSignature().getName() + " within " + (afterRun - beforeRun) + " ms");

    }
}

===
public @interface RequireNonsense {

    String prefix() default "";

}

===
@GetMapping(value = "/by-id/{id}")
    @RequireNonsense(prefix = "hehe")
    public Result getBookById(@PathVariable("id") int id) {
        return bookService.getBook(id);
    }
    
    remove checkNonsense method
