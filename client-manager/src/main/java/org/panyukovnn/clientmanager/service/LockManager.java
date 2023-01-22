package org.panyukovnn.clientmanager.service;

import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class LockManager {

    private final IMap<String, String> lockMap;

    public <T> T lockSupplier(String lockKey, Supplier<T> supplier){
        try {
            if (lockMap.tryLock(lockKey, 1, TimeUnit.SECONDS)) {
                return supplier.get();
            } else {
                throw new RuntimeException("Can't take the lock for key " + lockKey);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lockMap.unlock(lockKey);
        }
    }
}