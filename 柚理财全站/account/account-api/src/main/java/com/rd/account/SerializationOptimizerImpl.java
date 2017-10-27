package com.rd.account;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountBatchModel;
import com.rd.account.model.AccountModel;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.model.AccountRegisterModel;

/**
 * This class must be accessible from both the provider and consumer
 *
 * @author lishen
 */
public class SerializationOptimizerImpl implements SerializationOptimizer {

    @SuppressWarnings("rawtypes")
	public Collection<Class> getSerializableClasses() {
        List<Class> classes = new LinkedList<Class>();
        classes.add(AccountRegisterModel.class);
        classes.add(AccountQueryModel.class);
        classes.add(AccountModel.class);
        classes.add(Account.class);
        classes.add(AccountLog.class);
        classes.add(List.class);
        classes.add(AccountBatchModel.class);
        return classes;
    }
}
