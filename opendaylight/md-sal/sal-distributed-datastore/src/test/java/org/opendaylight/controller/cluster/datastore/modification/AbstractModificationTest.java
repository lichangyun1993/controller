/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.cluster.datastore.modification;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.Optional;
import org.junit.Before;
import org.opendaylight.controller.md.cluster.datastore.model.TestModel;
import org.opendaylight.mdsal.dom.spi.store.DOMStoreReadTransaction;
import org.opendaylight.mdsal.dom.spi.store.DOMStoreThreePhaseCommitCohort;
import org.opendaylight.mdsal.dom.spi.store.DOMStoreWriteTransaction;
import org.opendaylight.mdsal.dom.store.inmemory.InMemoryDOMDataStore;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier;
import org.opendaylight.yangtools.yang.data.api.schema.NormalizedNode;

public abstract class AbstractModificationTest {

    protected InMemoryDOMDataStore store;

    @Before
    public void setUp() {
        store = new InMemoryDOMDataStore("test", MoreExecutors.newDirectExecutorService());
        store.onGlobalContextUpdated(TestModel.createTestContext());
    }

    protected void commitTransaction(final DOMStoreWriteTransaction transaction) {
        DOMStoreThreePhaseCommitCohort cohort = transaction.ready();
        cohort.preCommit();
        cohort.commit();
    }

    protected Optional<NormalizedNode<?, ?>> readData(final YangInstanceIdentifier path) throws Exception {
        DOMStoreReadTransaction transaction = store.newReadOnlyTransaction();
        ListenableFuture<Optional<NormalizedNode<?, ?>>> future = transaction.read(path);
        return future.get();
    }
}
