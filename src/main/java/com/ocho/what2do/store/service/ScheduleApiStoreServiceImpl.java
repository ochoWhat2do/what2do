package com.ocho.what2do.store.service;

import com.ocho.what2do.common.file.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleApiStoreServiceImpl implements ScheduleApiStoreService {

  private final StoreService storeService;

  @Value("${image.fileUpload.path}")
  private String imageFileUploadPath;

  public List<File> getAllImageFiles() throws IOException {
    String imagePath = imageFileUploadPath != null ?
        imageFileUploadPath.equals("localUploadPath")  ?
            new ClassPathResource("image/stores").getFile().getAbsolutePath() :
            imageFileUploadPath
        : new ClassPathResource("image/stores").getFile().getAbsolutePath();

    // images 폴더 내의 모든 파일을 가져와서 리스트에 저장합니다.
    List<File> imageFiles = Files.walk(new File(imagePath).toPath())
        .filter(Files::isRegularFile)
        .filter(path -> !path.toFile().getName().equals("what2do_logo.png")) // 특정 파일 제외
        .map(path -> path.toFile())
        .collect(Collectors.toList());

    return imageFiles;
  }

  @Override
  @Transactional
  public int UpdateApiStoreImages() throws IOException {
    List<File> files = this.getAllImageFiles();
    List<MultipartFile> multipartFiles = new ArrayList<>();
    int result = 0;
    if (files.size() > 0) {
      MultipartFile file = null;
      List<String> storeKeyList = new ArrayList<>();
      for (int i = 0; i < files.size(); i++) {

        // 정규 표현식 패턴 정의
        Pattern pattern = Pattern.compile("\\d+"); // 숫자 하나 이상을 나타내는 패턴
        String fileName = files.get(i).getName(); // 예시 파일 이름
        String extractedNumber = "";
        // 정규 표현식과 매치하여 숫자 추출
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
          extractedNumber = matcher.group();
        } else {
          System.out.println("숫자를 찾을 수 없습니다.");
        }
        if (!extractedNumber.isEmpty()) {
          file = new CustomMultipartFile(files.get(i).getName()
              , files.get(i).getName()
              , "multipart/form-data", Files.readAllBytes(files.get(i).toPath()));
          multipartFiles.add(file);
          storeKeyList.add(extractedNumber);
        }
      }
      if (storeKeyList.size() > 0) {
        result = storeService.updateApiStore(storeKeyList, multipartFiles);
      }
    }

    return result;
  }
}
