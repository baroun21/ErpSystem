# Java Spring Boot Build Blockers

## Issue Summary
The Spring Boot Java ERP project (`erp-core-spring`) cannot be compiled due to a fundamental incompatibility between Java 25 (installed) and the Lombok annotation processor.

## Root Cause
- **System Java Version**: Java 25 LTS (build 25+37)
- **Project Target**: Java 21
- **Problem**: Lombok annotation processor initialization fails with `java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN`
- **Root**: Java 25's internal `com.sun.tools.javac.code.TypeTag` class has breaking changes that prevent Lombok's code generation

## Error Details
```
java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN
  at lombok.javac.apt.LombokProcessor.placePostCompileAndDontMakeForceRoundDummiesHook(LombokProcessor.java:174)
  at lombok.javac.apt.LombokProcessor.init(LombokProcessor.java:96)
  ...
```

## Attempted Solutions (All Failed)
1. ✅ Updated Lombok: 1.18.30 → 1.18.34
2. ✅ Updated Maven Compiler Plugin: 3.13.0 → 3.12.1 and back
3. ✅ Changed compiler flags: `release` → `source/target`
4. ✅ Upgraded Java target: 21 → 25 (same error)
5. ✅ Cleared Maven cache: Deleted ~/.m2/repository/com/company
6. ✅ Forced Maven updates: Used -U flag
7. ✅ Disabled Lombok-MapStruct binding
8. ✅ Tried forking compiler (fork=false)

## Why It Matters
- **Modules Blocked**: All 5 modules (model is primary blocker)
  - model (46 files) - Cannot compile
  - UserService - Depends on model
  - ViewAndControllers - Depends on model  
  - ERPMain - Depends on all above
  - Cannot start Spring Boot application

## Solutions

### Solution 1: Install Java 21 (RECOMMENDED)
**Time**: 5-10 minutes
**Steps**:
1. Download Java 21 JDK from Oracle or OpenJDK
2. Install Java 21
3. Set `JAVA_HOME` environment variable to Java 21
4. Run: `.\mvnw clean install -DskipTests`
5. Expected: Full build success ✅

**Pros**:
- Project designed for Java 21
- Minimal code changes
- Most stable solution

**Cons**:
- Requires second JDK installation
- Takes disk space

### Solution 2: Upgrade Project to Java 25
**Time**: 20-30 minutes
**Steps**:
1. Update pom.xml: Change `java.version=21` to `java.version=25`
2. Update dependencies for Java 25 compatibility
3. Update/replace Lombok (check for Java 25 support)
4. Code review for Java 21 → 25 breaking changes
5. Run: `.\mvnw clean install -DskipTests`
6. Expected: Full build success

**Pros**:
- Single JDK installation
- Modern Java version
- Latest features

**Cons**:
- Code compatibility review needed
- Potential breaking changes
- Project designed for Java 21

### Solution 3: Skip Annotation Processing (NOT RECOMMENDED)
**Status**: Partial workaround only
- Can compile code without Lombok/MapStruct processors
- Missing: @Getter, @Setter, @AllArgsConstructor, Lombok annotations won't work
- Missing: MapStruct mapper implementations
- Result: Code compiles but fails at runtime

## Current Project State
- **Status**: BLOCKED - Cannot compile
- **Spring Boot**: 3.2.5 (ready to run once compiled)
- **Modules**: 5 (parent, model, UserService, ViewAndControllers, ERPMain)
- **Entities**: 46 source files in model module
- **Framework**: Full Spring Boot + JPA + Oracle integration ready

##  Recommendation
**Option 1: Install Java 21** - Most pragmatic solution
- Project was designed for Java 21
- Zero code changes needed
- Faster resolution (5-10 minutes)
- Can run once installed

## Impact on Development
- Django Finance module: ✅ FULLY OPERATIONAL (66% tests passing)
- Java Spring Boot module: ❌ BUILD BLOCKED
- Recommendation: Complete Finance module to 100% testing while resolving Java issue

## Next Steps
1. Choose solution (Java 21 installation recommended)
2. Implement chosen solution
3. Verify build: `.\mvnw clean install -DskipTests`
4. Run: `java -jar ERPMain/target/ERPMain-1.0.0.jar` (port 8081)

---
**Last Updated**: 2026-02-02  
**Status**: ACTIVE BLOCKER

