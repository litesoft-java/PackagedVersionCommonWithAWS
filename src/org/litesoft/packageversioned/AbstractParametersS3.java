package org.litesoft.packageversioned;

public abstract class AbstractParametersS3 extends AbstractParameters {
    protected ParameterDeploymentGroup mDeploymentGroup = new ParameterDeploymentGroup();
    protected ParameterBucket mBucket = new ParameterBucket();

    public String getDeploymentGroup() {
        return mDeploymentGroup.get();
    }

    public ParameterBucket getParameterBucket() {
        return mBucket;
    }

    public String getBucket() {
        return mBucket.get();
    }
}
