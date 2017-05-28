package com.framgia.mobileprototype.data.source;

import android.text.TextUtils;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.data.source.project.ProjectRepository;
import com.framgia.mobileprototype.explore.Download;
import com.framgia.mobileprototype.util.EntryIdUtil;
import com.framgia.mobileprototype.util.ScreenSizeUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 24/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source
 */
public class DataImport {
    private ProjectRepository mProjectRepository;
    private MockRepository mMockRepository;
    private ElementRepository mElementRepository;

    public DataImport(ElementRepository elementRepository,
                      MockRepository mockRepository,
                      ProjectRepository projectRepository) {
        mElementRepository = elementRepository;
        mMockRepository = mockRepository;
        mProjectRepository = projectRepository;
    }

    public void save(InputStream inputStream) {
        if (inputStream == null) return;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) result.append(line);
            Project project = new Gson().fromJson(result.toString(), Project.class);
            save(project);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Project save(Project project) {
        float scaleWidth, scaleHeight;
        if (project.getOrientation().equals(Project.LANDSCAPE)) {
            scaleWidth = (float) ScreenSizeUtil.sHeight / project.getWidth();
            scaleHeight = (float) ScreenSizeUtil.sWidth / project.getHeight();
            project.setWidth(ScreenSizeUtil.sHeight);
            project.setHeight(ScreenSizeUtil.sWidth);
        } else {
            scaleWidth = (float) ScreenSizeUtil.sWidth / project.getWidth();
            scaleHeight = (float) ScreenSizeUtil.sHeight / project.getHeight();
            project.setWidth(ScreenSizeUtil.sWidth);
            project.setHeight(ScreenSizeUtil.sHeight);
        }
        long projectId;
        projectId = mProjectRepository.saveData(project);
        if (projectId == DataHelper.INSERT_ERROR) return null;
        for (int i = 0; i < project.getMocks().size(); i++) {
            Mock mock = project.getMocks().get(i);
            mock.setProjectId(String.valueOf(projectId));
            long mockId = mMockRepository.saveData(mock);
            if (mockId == DataHelper.INSERT_ERROR) continue;
            for (int j = 0; j < mock.getElements().size(); j++) {
                Element element = mock.getElements().get(j);
                if (element.getGesture() == null) {
                    element.setGesture(Constant.DEFAULT_GESTURE);
                }
                element.setMockId(String.valueOf(mockId));
                element.setX((int) (element.getX() * scaleWidth));
                element.setY((int) (element.getY() * scaleHeight));
                element.setWidth((int) (element.getWidth() * scaleWidth));
                element.setHeight((int) (element.getHeight() * scaleHeight));
                element.setEntryId(null);
                mElementRepository.saveData(mock.getElements().get(j));
            }
        }
        project.setId(String.valueOf(projectId));
        project.setNumberMocks(project.getMocks().size());
        return project;
    }

    public Project changeImageName(List<Download> downloads, String json) {
        Project project = new Gson().fromJson(json, Project.class);
        project.setEntryId(EntryIdUtil.get());
        String name = project.getEntryId() + Constant.DEFAULT_COMPRESS_FORMAT;
        downloads.add(new Download(name, project.getPoster()));
        project.setPoster(name);
        List<Element> elements = new ArrayList<>();
        for (Mock mock : project.getMocks()) {
            for (Element element : mock.getElements()) {
                elements.add(element);
            }
        }
        for (int i = 0; i < project.getMocks().size(); i++) {
            String mockEntryid = EntryIdUtil.get() + i;
            Mock mock = project.getMocks().get(i);
            for (Element element : elements) {
                if (TextUtils.isEmpty(element.getLinkTo())) continue;
                if (!element.getLinkTo().equals(mock.getEntryId())) continue;
                element.setLinkTo(mockEntryid);
            }
            mock.setEntryId(mockEntryid);
            String mockName = mock.getEntryId() + Constant.DEFAULT_COMPRESS_FORMAT;
            downloads.add(new Download(mockName, mock.getImage()));
            mock.setImage(mockName);
        }
        return project;
    }
}
