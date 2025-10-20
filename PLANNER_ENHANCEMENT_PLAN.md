# Planner Enhancement Implementation Plan

## Current State Analysis
- **Class_Planner**: Minimal model with only `id`, `cropId`, `startDate`
- **Activity_CreatePlannerActivity**: Uses Volley API with external PHP backend
- **AdapterPlanner**: Simple adapter displaying only start date
- **Data Storage**: External PHP API, not integrated with Firestore
- **Limitations**: No end date, planting method, notifications, or Firestore integration

## Enhancement Goals
1. Expand data model to include end date, planting method, notifications flag
2. Migrate from PHP API to Firestore database
3. Implement notification scheduling system
4. Enhance UI with new fields and better information display

---

## Implementation Tasks

### Phase 1: Data Model Expansion
- [x] Examine Class_Planner.java
- [ ] Add fields: `endDate`, `plantingMethod`, `notificationsEnabled`, `cropName`, `createdAt`
- [ ] Create enums for PlantingMethod (SABONG_TANIM, LIPAT_TANIM)
- [ ] Update constructor and getters/setters

### Phase 2: Firestore Integration
- [ ] Create FirestorePlannerHelper.java following FirestoreCropHelper pattern
- [ ] Implement methods:
  - `createPlan()` - Store plan in Firestore
  - `loadPlansByCropId()` - Query plans with callback
  - `updatePlan()` - Modify existing plan
  - `deletePlan()` - Remove plan
  - `getPlanSteps()` - Load procedure steps for plan
- [ ] Firestore collection structure: `plans/{planId}`
- [ ] Plan document fields: cropId, cropName, startDate, endDate, plantingMethod, notificationsEnabled, createdAt

### Phase 3: Activity Updates
- [ ] Update Activity_CreatePlannerActivity:
  - Replace Volley API calls with Firestore integration
  - Add end date picker (or auto-calculate from crop duration)
  - Add RadioButton/Spinner for planting method selection
  - Add Checkbox for notifications
  - Update layout reference to activity_create_planner.xml
  - Handle Firestore callbacks properly

### Phase 4: Notification System
- [ ] Create PlannerNotificationManager.java
  - Load plan and its procedure steps
  - Calculate notification dates: startDate + step.daysNotif
  - Use WorkManager or AlarmManager to schedule
  - Handle notification delivery

### Phase 5: UI Enhancements
- [ ] Update activity_create_planner.xml:
  - Add end date picker
  - Add planting method RadioButtons/Spinner
  - Add notifications checkbox
  - Improve layout structure
- [ ] Update activity_plan_item.xml:
  - Display crop name
  - Show planting method
  - Show notification status
  - Show date range
- [ ] Update AdapterPlanner to display new fields

### Phase 6: Testing & Verification
- [ ] Test plan creation with different methods
- [ ] Verify Firestore data persistence
- [ ] Test notification scheduling
- [ ] Validate UI displays correctly
- [ ] Test plan deletion/updates

---

## Technical Approach

### Firestore Collection Structure
```
collections/
  ├── plans/
      ├── {planId}/
          ├── cropId: string
          ├── cropName: string
          ├── startDate: timestamp
          ├── endDate: timestamp
          ├── plantingMethod: string (SABONG_TANIM or LIPAT_TANIM)
          ├── notificationsEnabled: boolean
          ├── createdAt: timestamp
```

### Pattern to Follow
Use FirestoreCropHelper as template:
- Callback interface for async operations
- Error handling with try-catch
- Field conversion for array types
- Proper logging with TAG

### Notification Calculation
- Get plan from Firestore
- Query procedures collection for cropId
- For each procedure: calculate date = startDate + daysNotif days
- Schedule WorkManager/AlarmManager task

---

## Expected Outcomes
✓ Plans stored locally in Firestore
✓ Enhanced data model supporting more crop planning features
✓ Automatic notifications for cultivation steps
✓ Improved UI showing all plan details
✓ Seamless integration with existing Firestore crops data
