import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvaluateAssignedReportsCoordinatorComponent } from './evaluate-assigned-reports-coordinator.component';

describe('EvaluateAssignedReportsCoordinatorComponent', () => {
  let component: EvaluateAssignedReportsCoordinatorComponent;
  let fixture: ComponentFixture<EvaluateAssignedReportsCoordinatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EvaluateAssignedReportsCoordinatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EvaluateAssignedReportsCoordinatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
