package com.kpro.common.utils;

import org.springframework.beans.factory.annotation.Value;

public class K8sSnowflakeGenerator {
  private final Snowflake generator;
  // Lấy NODE_NAME từ biến môi trường Kubernetes
  @Value("${K8S_NODE_NAME:default-node}")
  private String nodeName;
  // Lấy POD_NAME từ biến môi trường Kubernetes
  @Value("${K8S_POD_NAME:default-pod}")
  private String podName;
  // Lấy NAMESPACE_NAME từ biến môi trường Kubernetes
  @Value("${K8S_NAMESPACE_NAME:default-namespace}")
  private String namespaceName;
  @Value("${REPLICA_INDEX}")
  private Integer replicaIndex; // Lấy replica index của pod từ Kubernetes

  public K8sSnowflakeGenerator() {
    this.generator = new Snowflake(this.getNodeIdFromK8s());
  }

  /**
   * - name: POD_NAME valueFrom: fieldRef: fieldPath: metadata.name - name: NODE_NAME valueFrom:
   * fieldRef: fieldPath: spec.nodeName - name: POD_UID valueFrom: fieldRef: fieldPath: metadata.uid
   * - name: POD_NAMESPACE valueFrom: fieldRef: fieldPath: metadata.namespace
   */
  private int getNodeIdFromK8s() {
    if (podName == null || nodeName == null || namespaceName == null) {
      throw new IllegalStateException(
          "POD_NAME, NODE_NAME, NAMESPACE_NAME or REPLICA_INDEX environment variables are not set");
    }
    // Chuyển nodeName thành một giá trị số nguyên duy nhất cho nodeId
    return Math.abs(
        (namespaceName + nodeName + podName).hashCode()
            % 1024); // Giới hạn nodeId trong phạm vi 0-1023
  }

  /**
   * Setting replica index in deployment env: - name: REPLICA_INDEX valueFrom: fieldRef: fieldPath:
   * metadata.labels['pod-template-hash']
   *
   * <p>Không khả dụng vì giá trị này là string
   */
  @SuppressWarnings("unused")
  private int getMachineIdByReplicaIndex() {
    if (this.replicaIndex == null) {
      throw new IllegalStateException("REPLICA_INDEX environment variables are not set");
    }
    return this.replicaIndex;
  }

  public long nextId() {
    return this.generator.nextId();
  }
}
