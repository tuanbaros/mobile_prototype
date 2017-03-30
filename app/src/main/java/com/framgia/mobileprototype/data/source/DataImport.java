package com.framgia.mobileprototype.data.source;

import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.data.source.project.ProjectRepository;
import com.framgia.mobileprototype.util.ScreenSizeUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
            float scaleWidth = (float) ScreenSizeUtil.sChildWidth / project.getWidth();
            float scaleHeight = (float) ScreenSizeUtil.sChildHeight / project.getHeight();
            if (project.getOrientation().equals(Project.LANDSCAPE)) {
                project.setWidth(ScreenSizeUtil.sHeight);
                project.setHeight(ScreenSizeUtil.sWidth);
            } else {
                project.setWidth(ScreenSizeUtil.sWidth);
                project.setHeight(ScreenSizeUtil.sHeight);
            }
            long projectId = mProjectRepository.saveData(project);
            if (projectId == DataHelper.INSERT_ERROR) return;
            for (int i = 0; i < project.getMocks().size(); i++) {
                Mock mock = project.getMocks().get(i);
                mock.setProjectId(String.valueOf(projectId));
                mock.setPosition(i);
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
                    mElementRepository.saveData(mock.getElements().get(j));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader == null) return;
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
