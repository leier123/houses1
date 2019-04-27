package com.mooc.house.biz.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    public List<String> getImgPath(List<MultipartFile> files);
}
