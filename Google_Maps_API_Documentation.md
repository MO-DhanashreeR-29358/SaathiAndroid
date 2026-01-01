# SAATHI Android Project - Google Maps API Documentation

## 📋 Project Information
- **Project Name:** SAATHI Android Application
- **Package:** `com.mosl.tabapp`
- **Documentation Date:** December 2024
- **Version:** 1.0

---

## 🗝️ Google Maps API Keys

### Active API Key
```
AIzaSyBMQAiQWGdFOTRPecAwybutD8O7XMdUhiQ
```
**Location:** `app/src/main/AndroidManifest.xml`
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    tools:replace="android:value"
    android:value="AIzaSyBMQAiQWGdFOTRPecAwybutD8O7XMdUhiQ" />
```

### Alternative API Key (Commented)
```
AIzaSyCBwZ-oxAQ2ww1o7Y9zqpGGQZsGP1FBg3w
```

---

## 📦 Dependencies

### Google Play Services
```gradle
// Location Services
'location': "com.google.android.gms:play-services-location:21.0.1"

// Maps Services  
'maps': "com.google.android.gms:play-services-maps:18.1.0"

// Additional Play Services
'playService': "com.google.android.gms:play-services:23.1.2"
```

---

## 🔐 Required Permissions

```xml
<!-- Location Permissions -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```

---

## 🏗️ Core Implementation Classes

### 1. FusedLocation.java
**Path:** `app/src/main/java/com/mosl/tabapp/common/FusedLocation.java`
- **Purpose:** Primary location service handler
- **Key Features:**
  - Location updates with 1000ms intervals
  - High accuracy location requests
  - Permission handling
  - Settings resolution

### 2. UserLocationHelper.kt
**Path:** `app/src/main/java/com/mosl/tabapp/attendancedialog/UserLocationHelper.kt`
- **Purpose:** RxJava-based location helper
- **Key Features:**
  - BehaviorSubject for location events
  - Permission management
  - Address geocoding
  - Location settings validation

### 3. LocationWorker.kt
**Path:** `app/src/main/java/com/mosl/tabapp/service/location/LocationWorker.kt`
- **Purpose:** Background location tracking
- **Key Features:**
  - WorkManager integration
  - Periodic location updates

### 4. DataSyncWorker.kt
**Path:** `app/src/main/java/com/mosl/tabapp/service/location/DataSyncWorker.kt`
- **Purpose:** Location data synchronization
- **Key Features:**
  - Data upload to server
  - Background processing

### 5. GeoTaggingFragment.kt
**Path:** `app/src/main/java/com/mosl/tabapp/geoLocation/GeoTaggingFragment.kt`
- **Purpose:** Geo-tagging functionality
- **Key Features:**
  - Location-based tagging
  - UI for location selection

---

## 🔧 Location Services Configuration

### LocationRequest Settings
```kotlin
LocationRequest().apply {
    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    interval = 2000L  // 2 seconds
    fastestInterval = 2000L
}
```

### FusedLocationProviderClient Usage
```java
// Initialize client
mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

// Request location updates
mFusedLocationClient.requestLocationUpdates(
    mLocationRequest, 
    mLocationCallback, 
    Looper.myLooper()
);

// Get last known location
mFusedLocationClient.getLastLocation()
    .addOnSuccessListener(location -> {
        // Handle location
    });
```

---

## 🎯 Features Using Location APIs

### 1. Attendance Tracking
- **Files:** `AttendanceHelper.kt`, `UserLocationHelper.kt`
- **Purpose:** Track employee location during attendance marking
- **API Usage:** Real-time location updates, geofencing

### 2. Geo-tagging
- **Files:** `GeoTaggingFragment.kt`, `LocationItem.kt`
- **Purpose:** Tag locations for field activities
- **API Usage:** Location selection, address resolution

### 3. Background Location Monitoring
- **Files:** `ForegroundService.java`, `LocationWorker.kt`
- **Purpose:** Continuous location tracking
- **API Usage:** Background location updates, data synchronization

### 4. Location Validation
- **Files:** `MockLocationDetector.java`
- **Purpose:** Detect mock/fake locations
- **API Usage:** Location accuracy validation

### 5. Address Services
- **Files:** `UserLocationHelper.kt`
- **Purpose:** Convert coordinates to addresses
- **API Usage:** Geocoder for reverse geocoding

---

## 📊 Location Data Models

### LocationDataModel.kt
```kotlin
// Domain model for location data
data class LocationDataModel(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long,
    val address: String?
)
```

### LocationRequest.kt
```kotlin
// API request model
data class LocationRequest(
    val empCode: String,
    val latitude: Double,
    val longitude: Double,
    val locationTime: String
)
```

---

## ⚠️ Security Considerations

### API Key Security
- ✅ API keys are configured in AndroidManifest.xml
- ⚠️ **Recommendation:** Move API keys to secure storage or use restricted keys
- ⚠️ **Recommendation:** Implement API key rotation strategy

### Location Privacy
- ✅ Proper permission requests implemented
- ✅ User consent handling for location access
- ✅ Background location permission explicitly requested

### Mock Location Detection
- ✅ Mock location detection implemented
- ✅ Location accuracy validation

---

## 🔄 Data Flow

```
User Action → Location Request → FusedLocationProviderClient → 
Location Callback → Data Processing → Server Sync → UI Update
```

### Key Components:
1. **Location Request:** Triggered by user actions or background services
2. **Location Provider:** Google's FusedLocationProviderClient
3. **Data Processing:** Location validation and formatting
4. **Server Sync:** Upload location data to backend
5. **UI Update:** Reflect location status in user interface

---

## 📈 Performance Optimization

### Battery Optimization
- Location update intervals: 1-2 seconds
- Priority: HIGH_ACCURACY for precise tracking
- Background location limited to essential features

### Network Optimization
- Batch location uploads
- Offline location storage
- Retry mechanism for failed uploads

---

## 🐛 Error Handling

### Common Error Scenarios
1. **Location Permission Denied**
   - Graceful degradation
   - User guidance to settings

2. **Location Services Disabled**
   - Settings resolution dialog
   - Fallback mechanisms

3. **Network Connectivity Issues**
   - Offline data storage
   - Automatic retry logic

---

## 📝 API Usage Summary

| API Component | Usage | Files |
|---------------|-------|-------|
| FusedLocationProviderClient | Primary location service | FusedLocation.java, UserLocationHelper.kt |
| LocationRequest | Location configuration | Multiple files |
| LocationCallback | Location updates | FusedLocation.java, UserLocationHelper.kt |
| SettingsClient | Location settings | UserLocationHelper.kt |
| Geocoder | Address resolution | UserLocationHelper.kt |
| WorkManager | Background location | LocationWorker.kt, DataSyncWorker.kt |

---

## 📞 Support Information

**Generated By:** Amazon Q Developer  
**Date:** December 2024  
**Project Path:** `d:\Projects\SAATHI_ANDROID`  
**Documentation Version:** 1.0

---

*This document provides a comprehensive overview of Google Maps API usage in the SAATHI Android project. For technical support or updates, please refer to the development team.*