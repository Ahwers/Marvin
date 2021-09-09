package com.ahwers.marvin.framework.response;

// TODO: Make these more descriptive of what they mean, the failed ones are kind of all just synonymns.
// TODO: For instance, failed means that the application adaptor method is written erroneously, but failed doesn't adequately portray that.
public enum RequestOutcome {
	
	INVALID,
	SUCCESS,
	FAILED,
	UNMATCHED,
	OUTDATED,
	CONFLICTED,

}
