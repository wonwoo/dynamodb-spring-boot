package io.wonwoo.dynamodb;

import com.amazonaws.regions.Regions;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.data.dynamodb")
public class DynamoProperties {

  /**
   * aws accessKey
   */
  private String accessKey;

  /**
   * aws secretKey
   */
  private String secretKey;

  /**
   * regions default AP_NORTHEAST_2
   */
  private Regions regions = Regions.AP_NORTHEAST_2;

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public Regions getRegions() {
    return regions;
  }

  public void setRegions(Regions regions) {
    this.regions = regions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DynamoProperties that = (DynamoProperties) o;

    if (accessKey != null ? !accessKey.equals(that.accessKey) : that.accessKey != null) return false;
    if (secretKey != null ? !secretKey.equals(that.secretKey) : that.secretKey != null) return false;
    return regions == that.regions;
  }

  @Override
  public int hashCode() {
    int result = accessKey != null ? accessKey.hashCode() : 0;
    result = 31 * result + (secretKey != null ? secretKey.hashCode() : 0);
    result = 31 * result + (regions != null ? regions.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DynamoProperties{" +
        "accessKey='" + accessKey + '\'' +
        ", secretKey='" + secretKey + '\'' +
        ", regions=" + regions +
        '}';
  }
}