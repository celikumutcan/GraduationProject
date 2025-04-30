import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignInstructorsComponent } from './assign-instructors.component';

describe('AssignInstructorsComponent', () => {
  let component: AssignInstructorsComponent;
  let fixture: ComponentFixture<AssignInstructorsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignInstructorsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignInstructorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
