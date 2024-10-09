//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.shaotang.sequence.mysql;

import org.shaotang.sequence.ISequence;
import org.shaotang.sequence.dao.SequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.util.*;

public class LongSequence implements ISequence<Long> {
    private static final Logger logger = LoggerFactory.getLogger(LongSequence.class);
    private static Map<String, String> seqLockMap = new HashMap();
    private static Map<String, SequenceIdCache> seqCacheMap = new HashMap();
    SequenceService defaultSequenceService = new SequenceService();
    Connection connection;

    public void setDefaultSequenceService(SequenceService defaultSequenceService) {
        this.defaultSequenceService = defaultSequenceService;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Long nextVal(String sequenceName) {
        return (Long) this.nextVal(sequenceName, 1).get(0);
    }

    public List<Long> nextVal(String sequenceName, int count) {
        return this.nextVal(sequenceName, Long.MIN_VALUE, count);
    }

    public Long currentVal(String sequenceName) {
        if (sequenceName != null && !sequenceName.trim().isEmpty()) {
            PooledConnection pooledConnection = null;
            return defaultSequenceService.queryForLong(connection, sequenceName);
        } else {
            return null;
        }
    }

    public Long nextVal(String sequenceName, Long startValue) {
        return this.nextVal(sequenceName, startValue, 1).get(0);
    }

    public List<Long> nextVal(String sequenceName, Long startValue, int count) {
        if (sequenceName != null && sequenceName.trim().length() != 0 && count > 0) {
            String lockId = seqLockMap.get(sequenceName);
            if (lockId == null) {
                synchronized (logger) {
                    lockId = seqLockMap.get(sequenceName);
                    if (lockId == null) {
                        seqLockMap.put(sequenceName, sequenceName);
                        lockId = sequenceName;
                    }
                }
            }

            synchronized (lockId) {
                SequenceIdCache seqIdCache = seqCacheMap.get(sequenceName);
                if (seqIdCache != null && seqIdCache.getUnusedCount() >= (long) count) {
                    Long currentVal = seqIdCache.getMaxVal() - seqIdCache.getUnusedCount() + 1L;
                    seqIdCache.setUnusedCount(seqIdCache.getUnusedCount() - (long) count);
                    seqCacheMap.put(sequenceName, seqIdCache);
                    return this.generateIds(currentVal, count);
                } else {
                    return this.nextValue(sequenceName, startValue, count, seqIdCache);
                }
            }
        } else {
            return Collections.emptyList();
        }
    }

    private List<Long> generateIds(Long startValue, int count) {
        List<Long> ids = new ArrayList(count);

        for (int i = 0; i < count; ++i) {
            ids.add(startValue + (long) i);
        }

        return ids;
    }

    private List<Long> nextValue(String param1, Long param2, int param3, SequenceIdCache param4) {
        return Collections.emptyList();

    }

    private boolean insert(Connection conn, String sequenceName, long minvalue, int cacheSize) {
        long currentValue = minvalue + (long) cacheSize - 1L;
        return defaultSequenceService.insert(conn, sequenceName, currentValue, new Date(), Long.MAX_VALUE, minvalue);
    }


    private static class SequenceIdCache {
        private long maxVal;
        private long unusedCount;

        public SequenceIdCache(long currentVal, long unusedCount) {
            this.maxVal = currentVal;
            this.unusedCount = unusedCount;
        }

        public long getMaxVal() {
            return this.maxVal;
        }

        public long getUnusedCount() {
            return this.unusedCount;
        }

        public void setUnusedCount(long unusedCount) {
            this.unusedCount = unusedCount;
        }
    }
}
