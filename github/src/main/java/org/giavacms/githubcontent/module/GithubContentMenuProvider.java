package org.giavacms.githubcontent.module;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.module.MenuProvider;
import org.giavacms.base.common.module.MenuValue;
import org.giavacms.common.model.Search;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.repository.RichContentRepository;

@Named
@ApplicationScoped
public class GithubContentMenuProvider implements MenuProvider {

	@Override
	public List<String> getMenuItemSources() {
		List<String> list = new ArrayList<String>();
		list.add("githubContent");
		return list;
	}

	@Override
	public List<MenuValue> getMenuItemValues(String name) {
		return null;
	}

	@Override
	public String getName() {
		return "githubContent";
	}

}
