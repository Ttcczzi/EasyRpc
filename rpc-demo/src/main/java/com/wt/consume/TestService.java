package com.wt.consume;

import com.wt.interfaces.TestInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xcx
 * @date
 */
@Service
public class TestService {

    @Autowired
    public TestInterface testInterface;
}
