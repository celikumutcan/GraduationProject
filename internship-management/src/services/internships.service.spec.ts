import { TestBed } from '@angular/core/testing';

import { InternshipsService } from './internships.service';

describe('InternshipsService', () => {
  let service: InternshipsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InternshipsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
