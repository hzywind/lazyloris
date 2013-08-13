package com.lazyloris.mcp.model.repository;

import java.util.List;
import com.lazyloris.model.PersistenceRepository;
import com.lazyloris.mcp.model.*;

public interface Repository extends PersistenceRepository {
    
    Reply createReply();
    
    List<Reply> findAllReply();
    
    Reply loadReply(Long id);
    
    TextReply createTextReply();
    
    List<TextReply> findAllTextReply();
    
    TextReply loadTextReply(Long id);
    
}
