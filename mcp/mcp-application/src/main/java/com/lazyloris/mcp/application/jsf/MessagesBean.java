package com.lazyloris.mcp.application.jsf;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lazyloris.mcp.application.model.Message;
import com.lazyloris.mcp.application.model.np.MessageNP;
import com.lazyloris.mcp.application.model.np.NPUtils;
import com.lazyloris.mcp.application.model.repository.Repository;

@Component
@Scope("session")
public class MessagesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private Repository repository;
	
	@Transactional
	public List<MessageNP> getAllMessages() {
		List<Message> messages = repository.findAllMessage();
		return NPUtils.packageMessageList(messages);
	}

}
