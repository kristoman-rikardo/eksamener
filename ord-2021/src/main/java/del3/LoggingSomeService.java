package del3;

public class LoggingSomeService implements SomeService {
	private SomeService delegate;
	private Logger logger;
	// Add needed fields here

	/*
	 * Creates a LoggingSomeService object with the given delegate and logger
	 */
	public LoggingSomeService(SomeService delegate, Logger logger) {
		if (delegate == null || logger == null) throw new IllegalArgumentException();
		this.delegate = delegate;
		this.logger = logger;
	}

	@Override
	/**
	 * Delegates the job of calculating a magic string to the delegate, and logs the
	 * result before returning it
	 * 
	 * @return A string
	 */
	public String getAMagicString() {
		String result = this.delegate.getAMagicString();
		this.logger.log(result);
		return result;
	}

	/**
	 * Delegates the job of calculating a magic number to the delegate, and logs the
	 * result before returning it
	 * 
	 * @return An integer
	 */
	@Override
	public int getAMagicNumber() {
		int result = this.delegate.getAMagicNumber();
		this.logger.log(result + "");
		return result;
	}
	
	public static void main(String [] args) {
		Logger logger = new Logger();
		SomeService loggingService = new LoggingSomeService(new SomeServiceImpl(), logger);
		// This should print 42 twice (one for the logger, one for the return) 
		System.out.println(loggingService.getAMagicNumber());
		// This should print [42]
		System.out.println(logger.hasLogged);
		
	}
}
