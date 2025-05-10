package hu.indicium.speurtocht.controller;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream; /**
 * An InputStream wrapper that limits the number of bytes that can be read.
 */
public class LimitedInputStream extends FilterInputStream {
	private long remaining;

	public LimitedInputStream(InputStream in, long limit) {
		super(in);
		this.remaining = limit;
	}

	@Override
	public int read() throws IOException {
		if (remaining <= 0) {
			return -1;
		}

		int result = super.read();
		if (result != -1) {
			remaining--;
		}
		return result;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (remaining <= 0) {
			return -1;
		}

		int maxLen = (int) Math.min(len, remaining);
		int result = super.read(b, off, maxLen);
		if (result != -1) {
			remaining -= result;
		}
		return result;
	}

	@Override
	public long skip(long n) throws IOException {
		long skipped = super.skip(Math.min(n, remaining));
		remaining -= skipped;
		return skipped;
	}

	@Override
	public int available() throws IOException {
		return (int) Math.min(super.available(), remaining);
	}
}
