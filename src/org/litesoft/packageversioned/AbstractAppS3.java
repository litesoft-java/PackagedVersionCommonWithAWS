package org.litesoft.packageversioned;

import org.litesoft.aws.s3.*;
import org.litesoft.commonfoundation.exceptions.*;
import org.litesoft.commonfoundation.typeutils.*;
import org.litesoft.server.util.*;

import java.io.*;

public abstract class AbstractAppS3<Parameters extends AbstractParametersS3> extends AbstractApp<Parameters> {
    protected AbstractAppS3( String pAction, Parameters pParameters ) {
        super( pAction, pParameters );
    }

    protected Persister createPersister() {
        ParameterBucket zBucket = mParameters.getParameterBucket();
        try {
            return new S3Persister( BucketCredentials.with( zBucket.get() ).and( CONSOLE ).get( VersionedConfig.DEFAULT_PREFIXES ),
                                    new Bucket( zBucket.getS3Endpoint(), zBucket.get() ) );
        }
        catch ( IOException e ) {
            throw new WrappedIOException( e );
        }
    }

    protected String getDeploymentGroup() {
        return mParameters.getDeploymentGroup();
    }

    protected ParameterBucket getParameterBucket() {
        return mParameters.getParameterBucket();
    }

    protected String getBucket() {
        return mParameters.getBucket();
    }

    protected class Processor {
        protected final Persister mPersister;

        public Processor( Persister pPersister ) {
            mPersister = pPersister;
        }

        protected void extractVersionAndSet( String pFilePath ) {
            mParameters.getParameterVersion().set( VersionFile.fromFileLines( mPersister.getTextFile( pFilePath ) ).get() );
        }

        protected void writeDeploymentGroupVersionFiles() {
            createDeploymentGroupVersionFile( "-" + getVersion() );
            createDeploymentGroupVersionFile( "" );
        }

        private void createDeploymentGroupVersionFile( String pSpecificVersionSuffix ) {
            String zPath = createPath( getDeploymentGroup() + pSpecificVersionSuffix + ".txt" );
            CONSOLE.printLn( "Writing: " + zPath );
            mPersister.putTextFile( zPath, Strings.toLines( getVersion() ) );
        }
    }
}
