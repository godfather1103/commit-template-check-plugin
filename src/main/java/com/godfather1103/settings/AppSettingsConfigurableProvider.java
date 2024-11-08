package com.godfather1103.settings;

import com.godfather1103.ui.Settings;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2024</p>
 * <p>Company:      https://github.com/godfather1103</p>
 * 类描述：
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2024/11/1 18:00
 * @since 1.0
 */
public class AppSettingsConfigurableProvider extends ConfigurableProvider {

    @NotNull
    private final Project project;

    public AppSettingsConfigurableProvider(@NotNull Project project) {
        this.project = project;
    }

    @Override
    public @Nullable Configurable createConfigurable() {
        return new Settings(project);
    }
}
