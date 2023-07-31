package hu.indicium.speurtocht.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StartUpMultipartFile implements MultipartFile {

	private String name;
	private String originalFileName;
	private String contentType;
	private byte[] bytes;

	public StartUpMultipartFile(String name, String originalFileName, String contentType, byte[] bytes) {
		this.name = name;
		this.originalFileName = originalFileName;
		this.contentType = contentType;
		this.bytes = bytes;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getOriginalFilename() {
		return this.originalFileName;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public boolean isEmpty() {
		return this.bytes.length > 0;
	}

	@Override
	public long getSize() {
		return this.bytes.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.bytes);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {

	}
}
