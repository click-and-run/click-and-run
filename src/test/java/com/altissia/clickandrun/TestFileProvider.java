package com.altissia.clickandrun;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestFileProvider {
	public MockMultipartFile getXLSX(String file) throws IOException {
		File f;
		try {
			f = ResourceUtils.getFile(this.getClass().getResource(file));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unable to find file: {}" + file);
		}
		Path path = f.toPath();

		if (Files.notExists(path)) {
			throw new IllegalArgumentException("Unable to find file: " + path.toString());
		}

		String fileName = path.getFileName().toString();
		byte[] content = Files.readAllBytes(path);
		return new MockMultipartFile("file", fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content);
	}
}
