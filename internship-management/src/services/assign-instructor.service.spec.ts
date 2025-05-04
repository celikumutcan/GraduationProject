import { TestBed } from '@angular/core/testing';

import { AssignInstructorService } from './assign-instructor.service';

describe('AssignInstructorService', () => {
  let service: AssignInstructorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssignInstructorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
