package com.mosl.tabapp.query;

import com.mosl.app.core.data.query.QueryRepositoryImpl;
import com.mosl.mobile.domain.query.QueryRepository;
import dagger.Binds;
import dagger.Module;

@Module public abstract class QueryModule {
	@Binds abstract QueryView provideQueryContractView(QueryFragmentNew fragment);

	@Binds abstract QueryRepository provideQueryRepository(QueryRepositoryImpl repository);
}
